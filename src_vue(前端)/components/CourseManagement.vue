<template>
  <div class="course-management">
    <!-- 搜索表单 -->
    <el-form :inline="true">
      <el-form-item label="课程名称">
        <el-input v-model="searchParams.name" placeholder="请输入课程名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadCourses">搜索</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-button type="primary" @click="showAddDialog">添加课程</el-button>

    <!-- 课程表格 -->
    <el-table :data="courses">
      <!-- 表格列定义 -->
      <el-table-column prop="name" label="课程名称"></el-table-column>
      <el-table-column prop="credit" label="学分"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible">
      <el-form :model="currentCourse">
        <el-form-item label="课程名称">
          <el-input v-model="currentCourse.name"></el-input>
        </el-form-item>
        <el-form-item label="学分">
          <el-input-number v-model="currentCourse.credit" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="授课教师">
          <el-select v-model="currentCourse.teacherId" placeholder="请选择教师">
            <el-option
                v-for="teacher in teachers"
                :key="teacher.teacherId"
                :label="teacher.name"
                :value="teacher.teacherId">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCourse">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { ElMessage, ElMessageBox } from 'element-ui';
import service from '@/api/request'
export default {
  data() {
    return {
      // 搜索参数
      searchParams: {
        name: '',
        teacher: ''
      },

      // 课程数据
      courses: [],
      teachers: [],

      // 分页数据
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },

      // 对话框相关
      dialogVisible: false,
      dialogTitle: '',
      isEditMode: false,

      // 当前操作的课程
      currentCourse: {
        courseId: null,
        name: '',
        credit: 0,
        teacherId: null,
        description: ''
      }
    };
  },
  methods: {
    // 加载课程数据
    async loadCourses() {
      try {
        const response = await service.get('/courses', {
          params: {
            name: this.searchParams.name,
            page: this.pagination.currentPage - 1,
            size: this.pagination.pageSize
          }
        });
        this.courses = response.data.content || response.data;
        this.pagination.total = response.data.totalElements || response.data.length || 0;
      } catch (error) {
        console.error('加载课程数据失败', error);
        this.$message.error('加载课程数据失败: ' + (error.response?.data?.message || error.message));
      }
    },

    // 加载教师数据
    async loadTeachers() {
      try {
        const response = await service.get('/teachers');
        this.teachers = response.data;
      } catch (error) {
        console.error('加载教师数据失败', error);
        this.$message.error('加载教师数据失败');
      }
    },

    // 显示添加对话框
    showAddDialog() {
      this.dialogTitle = '添加课程';
      this.isEditMode = false;
      this.currentCourse = {
        courseId: null,
        name: '',
        credit: 0,
        teacherId: null,
        description: ''
      };
      this.dialogVisible = true;
    },

    // 显示编辑对话框
    handleEdit(course) {
      this.dialogTitle = '编辑课程';
      this.isEditMode = true;
      this.currentCourse = { ...course };
      this.dialogVisible = true;
    },

    // 提交课程表单
    async submitCourse() {
      try {
        if (this.isEditMode) {
          await service.put(`/courses/${this.currentCourse.courseId}`, this.currentCourse);
          this.$message.success('更新课程成功');
        } else {
          await service.post('/courses', this.currentCourse);
          this.$message.success('添加课程成功');
        }
        this.dialogVisible = false;
        this.loadCourses();
      } catch (error) {
        this.$message.error('操作失败: ' + (error.response?.data?.message || error.message));
      }
    },

    // 分页大小改变
    handleSizeChange(size) {
      this.pagination.pageSize = size;
      this.loadCourses();
    },

    // 当前页改变
    handleCurrentChange(page) {
      this.pagination.currentPage = page;
      this.loadCourses();
    }
  },
  mounted() {
    this.loadCourses();
    this.loadTeachers();
  }
};
</script>

<style scoped>
.course-management {
  padding: 20px;
}
</style>