package com.example.student_management.Service;

import com.example.student_management.Mapper.GithubCommitMapper;
import com.example.student_management.entity.GithubCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GithubCommitService {

    @Autowired
    private GithubCommitMapper commitMapper;


    // DTO classes for GitHub API response
    private static class GithubEvent {
        private String type;
        private Payload payload;

        public String getType() {
            return type;
        }

        public Payload getPayload() {
            return payload;
        }
    }

    private static class Payload {
        private List<CommitData> commits;

        public List<CommitData> getCommits() {
            return commits;
        }
    }

    private static class CommitData {
        private String sha;
        private String message;
        private Author author;
        private Commit commit;

        public String getSha() {
            return sha;
        }

        public String getMessage() {
            return message;
        }

        public Author getAuthor() {
            return author;
        }

        public Commit getCommit() {
            return commit;
        }
    }

    private static class Author {
        private String name;

        public String getName() {
            return name;
        }
    }

    private static class Commit {
        private CommitAuthor author;

        public CommitAuthor getAuthor() {
            return author;
        }
    }

    private static class CommitAuthor {
        private String date;

        public String getDate() {
            return date;
        }
    }

    // 同步学生GitHub提交记录
    public void syncStudentCommits(Integer studentId, String githubUsername) {
        List<GithubCommit> commits = fetchFromGithubAPI(githubUsername);
        commits.forEach(commit -> {
            commit.setStudentId(studentId);
            commitMapper.insert(commit);
        });
    }

    // 获取学生所有提交记录
    public List<GithubCommit> getStudentCommits(Integer studentId) {
        return commitMapper.selectByStudentId(studentId);
    }

    private List<GithubCommit> fetchFromGithubAPI(String username) {
        WebClient client = WebClient.create();

        return client.get()
                .uri("https://api.github.com/users/{username}/events", username)
                .header("Accept", "application/vnd.github.v3+json")
                .retrieve()
                .bodyToFlux(GithubEvent.class)
                .filter(event -> "PushEvent".equals(event.getType()))
                .flatMap(event -> Flux.fromIterable(event.getPayload().getCommits()))
                .map(commitData -> {
                    GithubCommit commit = new GithubCommit();
                    commit.setSha(commitData.getSha());
                    commit.setMessage(commitData.getMessage());
                    commit.setAuthor(commitData.getAuthor().getName());

                    // Parse GitHub date format
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd','HH:mm:ss','");
                    commit.setCommitDate(LocalDateTime.parse(commitData.getCommit().getAuthor().getDate(), formatter));

                    return commit;
                })
                .collectList()
                .block();
    }
}