<template>
  <div class="grade-report">
    <h2>成绩报表</h2>

    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane label="学生成绩单" name="student">
        <el-form :inline="true" class="search-form">
          <el-form-item label="学生">
            <el-select v-model="studentId" filterable placeholder="请选择学生">
              <el-option
                  v-for="student in students"
                  :key="student.studentId"
                  :label="`${student.name} (${student.studentId})`"
                  :value="student.studentId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadStudentReport">查询</el-button>
          </el-form-item>
        </el-form>

        <div v-if="studentReport" class="report-container">
          <h3>{{ studentReport.student.name }} 的成绩单</h3>
          <p>学号: {{ studentReport.student.studentId }}</p>
          <p v-if="studentReport.class">班级: {{ studentReport.class.name }}</p>

          <el-table :data="studentReport.scores" border style="width: 100%; margin-top: 20px;">
            <el-table-column prop="course.name" label="课程"></el-table-column>
            <el-table-column prop="score" label="成绩" width="100"></el-table-column>
            <el-table-column prop="semester" label="学期" width="150"></el-table-column>
          </el-table>

          <div style="margin-top: 20px; font-weight: bold;">
            平均分: {{ studentReport.average ? studentReport.average.toFixed(2) : '无' }}
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="班级成绩统计" name="class">
        <el-form :inline="true" class="search-form">
          <el-form-item label="班级">
            <el-select v-model="classId" filterable placeholder="请选择班级">
              <el-option
                  v-for="classItem in classes"
                  :key="classItem.classId"
                  :label="classItem.name"
                  :value="classItem.classId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadClassReport">查询</el-button>
          </el-form-item>
        </el-form>

        <div v-if="classReport" class="report-container">
          <h3>{{ classReport.class.name }} 成绩统计</h3>
          <p>专业: {{ classReport.class.major }}</p>
          <p>年级: {{ classReport.class.grade }}</p>

          <el-table :data="classReport.courseStats" border style="width: 100%; margin-top: 20px;">
            <el-table-column prop="course.name" label="课程"></el-table-column>
            <el-table-column prop="average" label="平均分" width="100">
              <template slot-scope="scope">
                {{ scope.row.average.toFixed(2) }}
              </template>
            </el-table-column>
            <el-table-column prop="max" label="最高分" width="100"></el-table-column>
            <el-table-column prop="min" label="最低分" width="100"></el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'student',
      studentId: null,
      classId: null,
      students: [],
      classes: [],
      studentReport: null,
      classReport: null
    }
  },
  mounted() {
    this.loadStudents()
    this.loadClasses()
  },
  methods: {
    // 加载学生数据
    async loadStudents() {
      try {
        const response = await axios.get('/students')
        this.students = response.data
      } catch (error) {
        this.$message.error('加载学生数据失败')
        console.error(error)
      }
    },

    // 加载班级数据
    async loadClasses() {
      try {
        const response = await axios.get('/api/classes')
        this.classes = response.data
      } catch (error) {
        this.$message.error('加载班级数据失败')
        console.error(error)
      }
    },

    // 加载学生成绩单
    async loadStudentReport() {
      if (!this.studentId) {
        this.$message.warning('请选择学生')
        return
      }

      try {
        const response = await axios.get(`/api/reports/student/${this.studentId}`)
        this.studentReport = response.data
      } catch (error) {
        this.$message.error('加载成绩单失败')
        console.error(error)
      }
    },

    // 加载班级成绩统计
    async loadClassReport() {
      if (!this.classId) {
        this.$message.warning('请选择班级')
        return
      }

      try {
        const response = await axios.get(`/api/reports/class/${this.classId}`)
        this.classReport = response.data
      } catch (error) {
        this.$message.error('加载班级统计失败')
        console.error(error)
      }
    }
  }
}
</script>

<style scoped>
.grade-report {
  padding: 20px;
}
.search-form {
  margin-bottom: 20px;
}
.report-container {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 4px;
  margin-top: 20px;
}
</style>