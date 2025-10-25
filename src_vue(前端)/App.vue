<template>
  <el-container>
    <el-header>
      <h1>学生成绩管理系统</h1>
    </el-header>
    <el-container>
      <el-aside width="200px">
        <el-menu
            :router="true"
            :default-active="$route.path"
            class="el-menu-vertical">
          <el-menu-item index="/students">
            <i class="el-icon-user"></i>
            <span slot="title">学生管理</span>
          </el-menu-item>
          <el-menu-item index="/scores">
            <i class="el-icon-document"></i>
            <span slot="title">成绩管理</span>
          </el-menu-item>
          <el-menu-item index="/courses">
            <i class="el-icon-notebook-2"></i>
            <span slot="title">课程管理</span>
          </el-menu-item>
          <el-menu-item index="/teachers">
            <i class="el-icon-user-solid"></i>
            <span slot="title">教师管理</span>
          </el-menu-item>
          <el-menu-item index="/classes">
            <i class="el-icon-office-building"></i>
            <span slot="title">班级管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      activeIndex: '/students'
    }
  },
  watch: {
    '$route'(to) {
      this.activeIndex = to.path
    }
  }
}
</script>

<style scoped>
el-header {
  background-color: #409EFF;
  color: white;
  line-height: 60px;
}

el-aside {
  background-color: #e6e6e6;
  height: calc(100vh - 60px);
}

el-main {
  padding: 20px;
}

.el-menu-vertical {
  border-right: none;
}

h1 {
  font-size: 20px;
  margin: 0;
}
</style>