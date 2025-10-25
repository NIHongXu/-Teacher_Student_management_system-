<template>
  <el-card class="github-commits-panel">
    <div slot="header" class="clearfix">
      <span>GitHub 提交记录</span>
      <el-button
          v-if="editable"
          type="primary"
          size="mini"
          @click="syncCommits"
          :loading="syncing"
          style="float: right; padding: 3px 10px">
        <i class="el-icon-refresh"></i> 同步
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-statistic title="活跃天数" :value="stats.activeDays || 0" />
      </el-col>
      <el-col :span="6">
        <el-statistic title="总提交" :value="stats.totalCommits || 0" />
      </el-col>
      <el-col :span="6">
        <el-statistic title="增加行数" :value="stats.totalAdded || 0" />
      </el-col>
      <el-col :span="6">
        <el-statistic title="删除行数" :value="stats.totalDeleted || 0" />
      </el-col>
    </el-row>

    <!-- 提交记录表格 -->
    <el-table
        :data="commits"
        border
        style="width: 100%; margin-top: 20px"
        v-loading="loading">
      <el-table-column prop="commitDate" label="日期" width="150">
        <template #default="{row}">
          {{ formatDate(row.commitDate) }}
        </template>
      </el-table-column>
      <el-table-column prop="repoUrl" label="仓库" width="180">
        <template #default="{row}">
          <a :href="row.repoUrl" target="_blank" class="repo-link">
            {{ formatRepoName(row.repoUrl) }}
          </a>
        </template>
      </el-table-column>
      <el-table-column prop="commitHash" label="Commit ID" width="120">
        <template #default="{row}">
          <a
              :href="`${row.repoUrl}/commit/${row.commitHash}`"
              target="_blank"
              class="commit-link">
            {{ row.commitHash.substring(0,7) }}
          </a>
        </template>
      </el-table-column>
      <el-table-column prop="commitMessage" label="提交信息" show-overflow-tooltip />
      <el-table-column label="变更" width="100" align="center">
        <template #default="{row}">
          <span class="lines-added">+{{ row.linesAdded }}</span> /
          <span class="lines-deleted">-{{ row.linesDeleted }}</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[10, 20, 50]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next"
        :total="pagination.total"
        style="margin-top: 20px">
    </el-pagination>
  </el-card>
</template>

<script>
import { getGithubCommits, syncGithubCommits } from '@/api/github'
import { formatDate } from '@/utils/date'

export default {
  name: 'GithubCommitsPanel',
  props: {
    studentId: {
      type: Number,
      required: true
    },
    githubUsername: {
      type: String,
      default: ''
    },
    editable: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      commits: [],
      stats: {},
      loading: false,
      syncing: false,
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  created() {
    this.fetchCommits()
    this.fetchStats()
  },
  methods: {
    formatDate,
    formatRepoName(url) {
      if (!url) return ''
      return url.split('/').slice(-2).join('/')
    },
    async fetchCommits() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage,
          size: this.pagination.pageSize
        }
        const res = await getGithubCommits(this.studentId, params)
        this.commits = res.data.content || res.data.list
        this.pagination.total = res.data.totalElements || res.data.total
      } catch (error) {
        console.error('获取提交记录失败:', error)
      } finally {
        this.loading = false
      }
    },
    async fetchStats() {
      try {
        // 获取最近30天的统计
        const endDate = new Date()
        const startDate = new Date()
        startDate.setDate(endDate.getDate() - 30)

        const res = await getGithubCommits(this.studentId, {
          stats: true,
          startDate: formatDate(startDate, 'yyyy-MM-dd'),
          endDate: formatDate(endDate, 'yyyy-MM-dd')
        })
        this.stats = res.data || {}
      } catch (error) {
        console.error('获取统计信息失败:', error)
      }
    },
    async syncCommits() {
      if (!this.githubUsername) {
        this.$message.warning('请先设置GitHub用户名')
        return
      }

      this.syncing = true
      try {
        await syncGithubCommits(this.studentId, {
          githubUsername: this.githubUsername
        })
        this.$message.success('同步成功')
        this.fetchCommits()
        this.fetchStats()
      } catch (error) {
        console.error('同步失败:', error)
        this.$message.error('同步失败: ' + (error.response?.data?.message || error.message))
      } finally {
        this.syncing = false
      }
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.fetchCommits()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.fetchCommits()
    }
  },
  watch: {
    studentId() {
      this.pagination.currentPage = 1
      this.fetchCommits()
      this.fetchStats()
    }
  }
}
</script>

<style scoped>
.github-commits-panel {
  margin-top: 20px;
}

.stats-row {
  margin-bottom: 15px;
}

.repo-link {
  color: #409EFF;
  text-decoration: none;
}

.commit-link {
  font-family: monospace;
  color: #67C23A;
  text-decoration: none;
}

.lines-added {
  color: #67C23A;
}

.lines-deleted {
  color: #F56C6C;
}

.el-statistic {
  text-align: center;
}
</style>