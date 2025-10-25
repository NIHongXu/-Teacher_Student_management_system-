// router/index.js
import Vue from 'vue'
import VueRouter from 'vue-router'
import StudentManagement from '../components/StudentManagement.vue';
import ScoreManagement from '../components/ScoreManagement.vue';
import CourseManagement from '../components/CourseManagement.vue';
import TeacherManagement from '../components/TeacherManagement.vue';
import ClassManagement from '../components/ClassManagement.vue';

Vue.use(VueRouter)

const routes = [
  {
    path: '/students',
    name: 'StudentManagement',
    component: StudentManagement
  },
  {
    path: '/scores',
    name: 'ScoreManagement',
    component: ScoreManagement
  },
  {
    path: '/courses',
    name: 'CourseManagement',
    component: CourseManagement
  },
  {
    path: '/teachers',
    name: 'TeacherManagement',
    component: TeacherManagement
  },
  {
    path: '/classes',
    name: 'ClassManagement',
    component: ClassManagement
  },
  {
    path: '/',
    redirect: '/students'
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});
export default router;