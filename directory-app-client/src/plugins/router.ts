import {createRouter, createWebHashHistory } from 'vue-router'
import ProfessionList from "../views/ProfessionList.vue";
import DepartmentList from "../views/DepartmentList.vue";
import EmployeeList from "../views/EmployeeList.vue";

const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        {
            path: '/',
            redirect: '/employees'
        },
        {
            path: '/professions',
            name: 'Professions',
            component: ProfessionList,
            meta: { title: 'Профессии' }
        },
        {
            path: '/departments',
            name: 'Departments',
            component: DepartmentList,
            meta: { title: 'Отделы' }
        },
        {
            path: '/employees',
            name: 'Employees',
            component: EmployeeList,
            meta: { title: 'Сотрудники' }
        }
    ]
})

export default router