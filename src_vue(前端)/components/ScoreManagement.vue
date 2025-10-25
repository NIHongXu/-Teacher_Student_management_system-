<template>
  <!-- 模板部分保持不变 -->
  <div class="score-management">
    <h2>成绩管理</h2>

    <!-- 搜索表单 -->
    <el-form :inline="true" class="search-form">
      <!-- 原有表单内容保持不变 -->
    </el-form>

    <div class="action-buttons">
       <el-button type="primary" @click="submitScore">确定</el-button>
    </div>
    <!-- 成绩表格 -->
    <el-table :data="scores" border style="width: 100%" @sort-change="handleSortChange">
      <!-- 原有表格列保持不变 -->
    </el-table>

    <!-- 分页 -->
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total">
    </el-pagination>

    <!-- 添加/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="50%">
      <!-- 原有对话框内容保持不变 -->
    </el-dialog>
  </div>
</template>

<script>
import service from '@/api/request'
import { Message, MessageBox } from 'element-ui';

export default {
  data() {
    return {
      scores: [],
      students: [],
      courses: [],
      dialogVisible: false,
      dialogTitle: '',
      isEditMode: false,
      currentScore: {
        scoreId: null,
        studentId: null,
        courseId: null,
        score: null,
        semester: ''
      },
      searchParams: {
        studentId: null,
        courseId: null,
        semester: ''
      },
      sortParams: {
        prop: null,
        order: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    };
  },
  methods: {
    // 加载成绩数据
    async loadScores() {
      try {
        const response = await service.get('/scores', {  // 移除多余的/api
          params: {
            semester: this.searchParams.semester,
            page: this.pagination.currentPage - 1,
            size: this.pagination.pageSize
          }
        });
        console.log('API响应数据:', response.data);

        this.scores = response.data.content || response.data;
        this.pagination.total = response.data.totalElements || response.data.total;
      } catch (error) {
        console.error('加载成绩数据失败', error);
        console.error('API错误详情:', error.response);
        this.$message.error('加载成绩数据失败');
      }
    },
    async loadStudents() {
      try {
        const response = await service.get('/students');  // 移除多余的/api
        this.students = response.data;
      } catch (error) {
        console.error('加载学生数据失败', error);
        this.$message.error('加载学生数据失败');
      }
    },
    async loadCourses() {
      try {
        const response = await service.get('/courses');  // 移除多余的/api
        this.courses = response.data;
      } catch (error) {
        console.error('加载课程数据失败', error);
        this.$message.error('加载课程数据失败');
      }
    },
    searchScores() {
      this.pagination.currentPage = 1;
      this.loadScores();
    },
    resetSearch() {
      this.searchParams = {
        studentId: null,
        courseId: null,
        semester: ''
      };
      this.sortParams = {
        prop: null,
        order: null
      };
      this.searchScores();
    },
    showAddDialog() {
      this.dialogTitle = '添加成绩';
      this.isEditMode = false;
      this.currentScore = {
        scoreId: null,
        studentId: null,
        courseId: null,
        score: null,
        semester: ''
      };
      this.dialogVisible = true;
    },
    handleEdit(score) {
      this.dialogTitle = '编辑成绩';
      this.isEditMode = true;
      this.currentScore = {
        scoreId: score.scoreId,
        studentId: score.student.studentId,
        courseId: score.course.courseId,
        score: score.score,
        semester: score.semester
      };
      this.dialogVisible = true;
    },
    async submitScore() {
      try {
        if (this.isEditMode) {
          await service.put(`/scores/${this.currentScore.scoreId}`, this.currentScore);
          Message.success('更新成绩成功');
        } else {
          await service.post('/scores', this.currentScore);
          Message.success('添加成绩成功');
        }
        this.dialogVisible = false;
        this.loadScores();
      } catch (error) {
        Message.error('操作失败');
        console.error(error);
      }
    },
    handleDelete(scoreId) {
      MessageBox.confirm('确定要删除这个成绩记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await service.delete(`/scores/${scoreId}`);
          Message.success('删除成功');
          this.loadScores();
        } catch (error) {
          Message.error('删除失败');
          console.error(error);
        }
      }).catch(() => {});
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size;
      this.loadScores();
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page;
      this.loadScores();
    },
    handleSortChange({ prop, order }) {
      this.sortParams = {
        prop,
        order: order === 'ascending' ? 'asc' : order === 'descending' ? 'desc' : null
      };
      this.loadScores();
    }
  },
  mounted() {
    this.loadScores();
    this.loadStudents();
    this.loadCourses();
  }
};
</script>

<style scoped>
.score-management {
  padding: 20px;
}
.search-form {
  margin-bottom: 20px;
}
</style>