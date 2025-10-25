<template>
  <div class="student-detail">
    <el-card>
      <div slot="header">
        <span>学生信息</span>
        <el-button
            type="primary"
            size="mini"
            @click="handleEdit"
            style="float: right;">
          编辑
        </el-button>
      </div>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="学号">{{ student.studentId }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ student.name }}</el-descriptions-item>
        <el-descriptions-item label="班级">{{ student.className }}</el-descriptions-item>
        <el-descriptions-item label="GitHub账号">
          <el-tag v-if="student.githubUsername" type="success">
            {{ student.githubUsername }}
          </el-tag>
          <el-tag v-else type="info">未设置</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 新增的GitHub提交面板 -->
    <github-commits-panel
        :student-id="student.studentId"
        :github-username="student.githubUsername"
        :editable="true"
        v-if="student.studentId"
        class="github-panel" />

    <!-- 编辑对话框 -->
    <el-dialog title="编辑学生信息" :visible.sync="dialogVisible" width="50%">
      <student-form
          :student="editForm"
          :classes="classes"
          @submit="handleSubmit"
          @cancel="dialogVisible = false" />
    </el-dialog>
  </div>
</template>

<script>
import { getStudentById } from '*/api/student'
import StudentForm from '*/views/StudentDetail'
import GithubCommitsPanel from '*/components/GithubCommitsPanel'

export default {
  name: 'StudentDetail',
  components: { StudentForm, GithubCommitsPanel },
  data() {
    return {
      student: {},
      classes: [],
      dialogVisible: false,
      editForm: {}
    }
  },
  created() {
    this.fetchStudent()
  },
  methods: {
    async fetchStudent() {
      const studentId = this.$route.params.id
      try {
        const res = await getStudentById(studentId)
        this.student = res.data
        this.editForm = { ...res.data }
      } catch (error) {
        console.error('获取学生信息失败:', error)
      }
    },
    handleEdit() {
      this.dialogVisible = true
    },
    async handleSubmit(form) {
      try {
        await updateStudent(form)
        this.$message.success('更新成功')
        this.dialogVisible = false
        this.fetchStudent()
      } catch (error) {
        console.error('更新失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.student-detail {
  padding: 20px;
}

.github-panel {
  margin-top: 20px;
}

.el-descriptions {
  margin-top: 20px;
}
</style>