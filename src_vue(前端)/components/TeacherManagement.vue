<template>
  <div class="teacher-management">
    <h2>教师管理</h2>

    <!-- 搜索表单 -->
    <el-form :inline="true" class="search-form">
      <el-form-item label="姓名">
        <el-input v-model="searchParams.name" placeholder="请输入教师姓名"></el-input>
      </el-form-item>
      <el-form-item label="院系">
        <el-input v-model="searchParams.department" placeholder="请输入院系"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchTeachers">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button type="primary" @click="showAddDialog">添加教师</el-button>
    </div>

    <!-- 教师表格 -->
    <el-table :data="teachers" border style="width: 100%">
      <el-table-column prop="teacherId" label="教师ID" width="100"></el-table-column>
      <el-table-column prop="name" label="姓名" width="120"></el-table-column>
      <el-table-column prop="gender" label="性别" width="80"></el-table-column>
      <el-table-column prop="title" label="职称" width="120"></el-table-column>
      <el-table-column prop="department" label="院系"></el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.teacherId)">删除</el-button>
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
      <el-form ref="teacherForm" :model="currentTeacher" :rules="rules" label-width="100px">
        <el-form-item label="姓名" required>
          <el-input v-model="currentTeacher.name"></el-input>
        </el-form-item>
        <el-form-item label="性别" required>
          <el-select v-model="currentTeacher.gender" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="职称">
          <el-input v-model="currentTeacher.title"></el-input>
        </el-form-item>
        <el-form-item label="院系">
          <el-input v-model="currentTeacher.department"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitTeacher">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import service from '@/api/request'  // 导入配置好的axios实例
import { ElMessage, ElMessageBox } from 'element-ui';

export default {
  data() {
    return {
      teachers: [],
      dialogVisible: false,
      dialogTitle: '',
      isEditMode: false,
      currentTeacher: this.getEmptyTeacher(),
      searchParams: {
        name: '',
        department: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入教师姓名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在2到20个字符', trigger: 'blur' }
        ],
        department: [
          { required: true, message: '请选择院系', trigger: 'change' }
        ]
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    getEmptyTeacher() {
      return {
        teacherId: null,
        name: '',
        gender: '',
        title: '',
        department: ''
      };
    },
    async loadTeachers() {
      try {
        const response = await service.get('/teachers', {
          params: this.searchParams
        });
        this.teachers = response.data;
        if (response.data.pagination) {
          this.pagination = response.data.pagination
        }
      } catch (error) {
        console.error('加载教师列表失败:', error)
        // 正确访问错误信息
        const errorMsg = error.response?.data?.message || error.message || '加载教师列表失败'
        this.$message.error(errorMsg)  // 使用Element UI的提示组件
      }
    },
    searchTeachers() {
      this.pagination.currentPage = 1;
      this.loadTeachers();
    },
    resetSearch() {
      this.searchParams = {
        name: '',
        department: ''
      };
      this.searchTeachers();
    },
    showAddDialog() {
      this.dialogTitle = '添加教师';
      this.isEditMode = false;
      this.currentTeacher = this.getEmptyTeacher();
      this.dialogVisible = true;
    },
    handleEdit(row) {
      this.dialogTitle = '编辑教师';
      this.currentTeacher = JSON.parse(JSON.stringify(row));
      this.dialogVisible = true;
    },
    async submitTeacher() {
      try {
        if (this.currentTeacher.teacherId) {
          // 更新
          await service.put(`/teachers/${this.currentTeacher.teacherId}`, this.currentTeacher)
        } else {
          // 新增
          await service.post('/teachers', this.currentTeacher)
        }
        this.$message.success('操作成功')
        this.dialogVisible = false
        this.loadTeachers() // 刷新列表
      } catch (error) {
        console.error('操作失败:', error)
        this.$message.error('操作失败')
      }
    },
    handleDelete(teacherId) {
      this.$confirm('确定要删除该教师吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await service.delete(`/teachers/${teacherId}`);
          this.$message.success('删除成功');
          this.loadTeachers(); // 刷新教师列表
        } catch (error) {
          this.handleDeleteError(error, teacherId);
        }
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },
    handleDeleteError(error, teacherId) {
      let errorMessage = '删除失败';

      if (error.response) {
        // 从后端响应中提取错误信息
        errorMessage = error.response.data?.message || errorMessage;

        // 特定状态码处理
        if (error.response.status === 404) {
          errorMessage = '教师不存在或已被删除';
        } else if (error.response.status === 403) {
          errorMessage = '没有权限删除此教师';
        }
      } else {
        errorMessage = error.message || errorMessage;
      }

      this.$message.error(errorMessage);
      console.error(`删除教师ID ${teacherId}失败:`, error);
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size;
      this.loadTeachers();
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page;
      this.loadTeachers();
    }
  },
  mounted() {
    console.log('表单引用:', this.$refs.teacherForm);
    this.loadTeachers();
  }
}
</script>

<style scoped>
.teacher-management {
  padding: 20px;
}
.search-form {
  margin-bottom: 20px;
}
.action-buttons {
  margin-bottom: 20px;
}
</style>