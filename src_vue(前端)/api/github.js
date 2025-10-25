import request from '@/utils/request'

export function getGithubCommits(studentId, params) {
    return request({
        url: `/api/students/${studentId}/github-commits`,
        method: 'get',
        params
    })
}

export function syncGithubCommits(studentId, data) {
    return request({
        url: `/api/students/${studentId}/github-commits/sync`,
        method: 'post',
        data
    })
}