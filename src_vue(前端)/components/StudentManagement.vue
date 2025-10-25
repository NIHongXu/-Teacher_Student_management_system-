<template>
  <!-- 模板部分保持不变 -->
  <div class="student-management">
    <h2>学生管理</h2>

    <!-- 搜索表单 -->
    <el-form :inline="true" class="search-form">
      <el-form-item label="姓名">
        <el-input v-model="searchParams.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="班级">
        <el-select v-model="searchParams.classId" placeholder="请选择班级">
          <el-option
              v-for="classItem in classes"
              :key="classItem.classId"
              :label="classItem.name"
              :value="classItem.classId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="searchParams.gender" placeholder="请选择性别">
          <el-option label="男" value="男"></el-option>
          <el-option label="女" value="女"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchStudents">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button type="primary" @click="showAddDialog">添加学生</el-button>
    </div>

    <!-- 学生表格 -->
    <el-table :data="students" border style="width: 100%">
      <el-table-column prop="studentId" label="学号" width="100"></el-table-column>
      <el-table-column prop="name" label="姓名" width="120"></el-table-column>
      <el-table-column prop="gender" label="性别" width="80"></el-table-column>
      <el-table-column prop="birthDate" label="出生日期" width="120"></el-table-column>
      <el-table-column prop="phone" label="电话" width="150"></el-table-column>
      <el-table-column prop="className" label="班级" width="150"></el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.studentId)">删除</el-button>
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
      <el-form :model="currentStudent" label-width="100px">
        <el-form-item label="姓名" required>
          <el-input v-model="currentStudent.name"></el-input>
        </el-form-item>
        <el-form-item label="性别" required>
          <el-select v-model="currentStudent.gender" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="出生日期">
          <el-date-picker v-model="currentStudent.birthDate" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="currentStudent.phone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="currentStudent.email"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="currentStudent.address" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="班级">
          <el-select v-model="currentStudent.classId" placeholder="请选择班级">
            <el-option
                v-for="classItem in classes"
                :key="classItem.classId"
                :label="classItem.name"
                :value="classItem.classId">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitStudent">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import service from '@/api/request'  // 导入配置好的axios实例

export default {
  data() {
    return {
      students: [],
      classes: [],
      dialogVisible: false,
      dialogTitle: '',
      isEditMode: false,
      currentStudent: {
        studentId: null,
        name: '',
        gender: '',
        birthDate: '',
        phone: '',
        email: '',
        address: '',
        classId: null
      },
      searchParams: {
        name: '',
        classId: null,
        gender: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    };
  },
  methods: {
    async loadStudents() {
      try {
        const response = await service.get('/students', {
          params: this.searchParams
        });
        this.students = response.data;
      } catch (error) {
        console.error('加载学生数据失败', error);
        this.$message.error(`加载失败: ${error.response?.data?.message || error.message}`);
      }
    },

    async loadClasses() {
      try {
        const response = await service.get('/students/classes');
        this.classes = response.data;
      } catch (error) {
        this.$message.error('加载班级数据失败');
        console.error(error);
      }
    },
    searchStudents() {
      this.pagination.currentPage = 1;
      this.loadStudents();
    },
    resetSearch() {
      this.searchParams = {
        name: '',
        classId: null,
        gender: ''
      };
      this.searchStudents();
    },
    showAddDialog() {
      this.dialogTitle = '添加学生';
      this.isEditMode = false;
      this.currentStudent = {
        studentId: null,
        name: '',
        gender: '',
        birthDate: '',
        phone: '',
        email: '',
        address: '',
        classId: null
      };
      this.dialogVisible = true;
    },
    handleEdit(student) {
      this.dialogTitle = '编辑学生';
      this.isEditMode = true;
      this.currentStudent = { ...student };
      this.dialogVisible = true;
    },
    async submitStudent() {
      try {
        if (this.isEditMode) {
          await service.put(`/students/${this.currentStudent.studentId}`, this.currentStudent);
          this.$message.success('更新学生成功');
        } else {
          await service.post('/students', this.currentStudent);
          this.$message.success('添加学生成功');
        }
        this.dialogVisible = false;
        this.loadStudents();
      } catch (error) {
        this.$message.error('操作失败');
        console.error(error);
      }
    },
    handleDelete(studentId) {
      this.$confirm('确定要删除这个学生吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await service.delete(`/students/${studentId}`);
          this.$message.success('删除成功');
          this.loadStudents();
        } catch (error) {
          this.$message.error('删除失败');
          console.error(error);
        }
      }).catch(() => {});
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size;
      this.loadStudents();
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page;
      this.loadStudents();
    }
  },
  mounted() {
    this.loadStudents();
    this.loadClasses();
  }
};
</script>

<style scoped>
.student-management {
  padding: 20px;
}
.search-form {
  margin-bottom: 20px;
}
.action-buttons {
  margin-bottom: 20px;
}
</style>