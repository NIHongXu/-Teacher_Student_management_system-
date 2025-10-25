package com.example.student_management.dto;

import lombok.Data;

@Data
public class GithubCommitStats {
    private Integer activeDays;      // 活跃天数
    private Integer totalCommits;    // 总提交次数
    private Integer totalAdded;      // 总增加行数
    private Integer totalDeleted;    // 总删除行数

    // 计算净增行数
    public Integer getNetLines() {
        return totalAdded - totalDeleted;
    }
}