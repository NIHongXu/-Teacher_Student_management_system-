<template>
  <!-- 模板部分保持不变 -->
  <div class="class-management">
    <h2>班级管理</h2>

    <!-- 搜索表单 -->
    <el-form :inline="true" class="search-form">
      <el-form-item label="班级名称">
        <el-input v-model="searchParams.name" placeholder="请输入班级名称"></el-input>
      </el-form-item>
      <el-form-item label="专业">
        <el-input v-model="searchParams.major" placeholder="请输入专业"></el-input>
      </el-form-item>
      <el-form-item label="年级">
        <el-input-number v-model="searchParams.grade" :min="2021" :max="2023"></el-input-number>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchClasses">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button type="primary" @click="showAddDialog">添加班级</el-button>
    </div>

    <!-- 班级表格 -->
    <el-table :data="classes" border style="width: 100%">
      <el-table-column prop="classId" label="班级ID" width="100"></el-table-column>
      <el-table-column prop="name" label="班级名称" width="150"></el-table-column>
      <el-table-column prop="major" label="专业" width="180"></el-table-column>
      <el-table-column prop="grade" label="年级" width="100"></el-table-column>
      <el-table-column prop="headTeacher.name" label="班主任" width="120"></el-table-column>
      <el-table-column label="操作" width="220">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" @click="showStudents(scope.row.classId)">查看学生</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.classId)">删除</el-button>
        </template>
      </el-table-column>
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
      <el-form :model="currentClass" label-width="100px">
        <el-form-item label="班级名称" required>
          <el-input v-model="currentClass.name"></el-input>
        </el-form-item>
        <el-form-item label="专业" required>
          <el-input v-model="currentClass.major"></el-input>
        </el-form-item>
        <el-form-item label="年级" required>
          <el-input-number v-model="currentClass.grade" :min="2021" :max="2023"></el-input-number>
        </el-form-item>
        <el-form-item label="班主任">
          <el-select v-model="currentClass.headTeacherId" filterable placeholder="请选择班主任">
            <el-option
                v-for="teacher in teachers"
                :key="teacher.teacherId"
                :label="teacher.name"
                :value="teacher.teacherId">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitClass">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 学生列表对话框 -->
    <el-dialog title="班级学生列表" :visible.sync="studentDialogVisible" width="70%">
      <el-table :data="classStudents" border style="width: 100%">
        <el-table-column prop="studentId" label="学号" width="100"></el-table-column>
        <el-table-column prop="name" label="姓名" width="120"></el-table-column>
        <el-table-column prop="gender" label="性别" width="80"></el-table-column>
        <el-table-column prop="birthDate" label="出生日期" width="120"></el-table-column>
        <el-table-column prop="phone" label="电话" width="150"></el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import service from '@/api/request'
import { ElMessage, ElMessageBox } from 'element-ui';

export default {
  data() {
    return {
      classes: [],
      teachers: [],
      classStudents: [],
      dialogVisible: false,
      studentDialogVisible: false,
      dialogTitle: '',
      isEditMode: false,
      currentClass: {
        classId: null,
        name: '',
        major: '',
        grade: new Date().getFullYear(),
        headTeacherId: null
      },
      searchParams: {
        name: '',
        major: '',
        grade: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    };
  },
  methods: {
    // 加载班级数据
    async loadClasses() {
      try {
        const response = await service.get('/classes', {
          params:{
            page: 1,
            size: 100
          }
        });
        this.classes = response.data.content || response.data;
        this.pagination.total = response.data.totalElements || response.data.length;
      } catch (error) {
        console.error('加载班级失败:', error);
        this.$message.error('加载班级数据失败: ' + error.message);

        if (error.response) {
          console.log('响应数据:', error.response.data);
          console.log('状态码:', error.response.status);
        }
      }
    },
    // 加载教师数据
    async loadTeachers() {
      try {
        const response = await service.get('/teachers');
        this.teachers = response.data;
      } catch (error) {
        ElMessage.error('加载教师数据失败');
        console.error(error);
      }
    },
    // 搜索班级
    searchClasses() {
      this.pagination.currentPage = 1;
      this.loadClasses();
    },
    // 重置搜索
    resetSearch() {
      this.searchParams = {
        name: '',
        major: '',
        grade: null
      };
      this.searchClasses();
    },
    // 显示添加对话框
    showAddDialog() {
      this.dialogTitle = '添加班级';
      this.isEditMode = false;
      this.currentClass = {
        classId: null,
        name: '',
        major: '',
        grade: new Date().getFullYear(),
        headTeacherId: null
      };
      this.dialogVisible = true;
    },
    // 处理编辑
    handleEdit(clazz) {
      this.dialogTitle = '编辑班级';
      this.isEditMode = true;
      this.currentClass = { ...clazz };
      this.dialogVisible = true;
    },
    // 提交班级信息
    async submitClass() {
      try {
        if (this.isEditMode) {
          await service.put(`/classes/${this.currentClass.classId}`, this.currentClass);
          ElMessage.success('更新班级成功');
        } else {
          await service.post('/classes', this.currentClass);
          ElMessage.success('添加班级成功');
        }
        this.dialogVisible = false;
        this.loadClasses();
      } catch (error) {
        ElMessage.error('操作失败');
        console.error(error);
      }
    },
    // 处理删除
    handleDelete(classId) {
      ElMessageBox.confirm('确定要删除这个班级吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await service.delete(`/classes/${classId}`);
          ElMessage.success('删除成功');
          this.loadClasses();
        } catch (error) {
          ElMessage.error('删除失败');
          console.error(error);
        }
      }).catch(() => {});
    },
    // 显示班级学生
    async showStudents(classId) {
      try {
        const response = await service.get(`/students/class/${classId}`);
        this.classStudents = response.data;
        this.studentDialogVisible = true;
      } catch (error) {
        ElMessage.error('加载学生数据失败');
        console.error(error);
      }
    },
    // 分页大小改变
    handleSizeChange(size) {
      this.pagination.pageSize = size;
      this.loadClasses();
    },
    // 当前页改变
    handleCurrentChange(page) {
      this.pagination.currentPage = page;
      this.loadClasses();
    }
  },
  mounted() {
    this.loadClasses();
    this.loadTeachers();
  }
};
</script>

<style scoped>
.class-management {
  padding: 20px;
}
.search-form {
  margin-bottom: 20px;
}
.action-buttons {
  margin-bottom: 20px;
}
</style>