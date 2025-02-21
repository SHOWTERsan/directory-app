<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { Employee } from '../domain/Employee.ts'
import type { Department } from '../domain/Department.ts'
import type { Profession } from '../domain/Profession.ts'
import { EmployeeService } from '../services/employee-service.ts'
import { DepartmentService } from '../services/department-service.ts'
import { ProfessionService } from '../services/profession-service.ts'

const employeeService = new EmployeeService()
const departmentService = new DepartmentService()
const professionService = new ProfessionService()

const employees = ref<Employee[]>([])
const showForm = ref(false)
const editingEmployee = ref<Employee | null>(null)
const loading = ref(false)
const departments = ref<Department[]>([])
const professions = ref<Profession[]>([])

function createNewEmployee(): Employee {
  return {
    fullName: '',
    professionId: 0,
    departmentId: 0,
    notes: ''
  }
}

const formEmployee = ref<Employee>(createNewEmployee())

async function loadEmployees() {
  loading.value = true
  try {
    employees.value = await employeeService.getAll()

    for (const employee of employees.value) {
      const departmentResponse = await departmentService.getById(employee.departmentId)
      employee.departmentName = departmentResponse.name

      const professionResponse = await professionService.getById(employee.professionId)
      employee.professionName = professionResponse.name
    }

  } catch (error) {
    console.error('Failed to load employees:', error)
  } finally {
    loading.value = false
  }
}


async function loadDepartmentsAndProfessions() {
  try {
    const [depsResponse, profsResponse] = await Promise.all([
      departmentService.getAll(),
      professionService.getAll()
    ]);
    departments.value = depsResponse
    professions.value = profsResponse
  } catch (error) {
    console.error('Failed to load departments or professions:', error)
  }
}

async function saveEmployee(employee: Employee) {
  try {
    if (editingEmployee.value?.id) {
      await employeeService.update(editingEmployee.value.id, employee)
    } else {
      await employeeService.create(employee)
    }
    await loadEmployees()
    closeForm()
  } catch (error) {
    console.error('Failed to save employee:', error)
  }
}

function closeForm() {
  showForm.value = false
  editingEmployee.value = null
  formEmployee.value = createNewEmployee()
}

async function deleteEmployee(id: number) {
  if (confirm('Вы уверены, что хотите удалить этого сотрудника?')) {
    try {
      await employeeService.delete(id)
      await loadEmployees()
    } catch (error) {
      console.error('Failed to delete employee:', error)
    }
  }
}

function editEmployee(employee: Employee) {
  editingEmployee.value = { ...employee }
  formEmployee.value = { ...employee }
  showForm.value = true
}
function showAddForm() {
  formEmployee.value = createNewEmployee()
  editingEmployee.value = null
  showForm.value = true
}

onMounted(() => {
  loadEmployees()
  loadDepartmentsAndProfessions()
})
</script>

<template>
  <div class="p-4">
    <div class="flex justify-between items-center mb-4">
      <h1 class="text-2xl font-bold">Сотрудники</h1>
      <button
          @click="showAddForm"
          class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
      >
        Добавить сотрудника
      </button>
    </div>

    <div v-if="loading" class="text-center py-4">Загрузка...</div>

    <table v-else class="min-w-full bg-white border">
      <thead>
      <tr>
        <th class="px-6 py-3 border-b text-left">ФИО</th>
        <th class="px-6 py-3 border-b text-left">Отдел</th>
        <th class="px-6 py-3 border-b text-left">Профессия</th>
        <th class="px-6 py-3 border-b text-left">Примечание</th>
        <th class="px-6 py-3 border-b text-right">Действия</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="employee in employees" :key="employee.id" class="hover:bg-gray-50">
        <td class="px-6 py-4 border-b">{{ employee.fullName }}</td>
        <td class="px-6 py-4 border-b">{{ employee.departmentName }}</td>
        <td class="px-6 py-4 border-b">{{ employee.professionName }}</td>
        <td class="px-6 py-4 border-b">{{ employee.notes }}</td>
        <td class="px-6 py-4 border-b text-right">
          <button
              @click="editEmployee(employee)"
              class="text-blue-500 hover:text-blue-700 mr-2"
          >
            Изменить
          </button>
          <button
              @click="deleteEmployee(employee.id)"
              class="text-red-500 hover:text-red-700"
          >
            Удалить
          </button>
        </td>
      </tr>
      </tbody>
    </table>

    <div v-if="showForm" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div class="bg-white p-6 rounded-lg w-full max-w-md">
        <h2 class="text-xl font-bold mb-4">
          {{ editingEmployee ? 'Изменить сотрудника' : 'Добавить сотрудника' }}
        </h2>
        <form @submit.prevent="saveEmployee(formEmployee)">
          <div class="mb-4">
            <label class="block mb-2">ФИО</label>
            <input
                v-model="formEmployee.fullName"
                required
                class="w-full border p-2 rounded"
            />
          </div>
          <div class="mb-4">
            <label class="block mb-2">Отдел</label>
            <select
                v-model="formEmployee.departmentId"
                required
                class="w-full border p-2 rounded"
            >
              <option
                  v-for="department in departments"
                  :key="department.id"
                  :value="department.id"
              >
                {{ department.name }}
              </option>
            </select>
          </div>
          <div class="mb-4">
            <label class="block mb-2">Профессия</label>
            <select
                v-model="formEmployee.professionId"
                required
                class="w-full border p-2 rounded"
            >
              <option
                  v-for="profession in professions"
                  :key="profession.id"
                  :value="profession.id"
              >
                {{ profession.name }}
              </option>
            </select>
          </div>

          <div class="mb-4">
            <label class="block mb-2">Примечание</label>
            <textarea
                v-model="formEmployee.notes"
                class="w-full border p-2 rounded"
            />
          </div>
          <div class="flex justify-end gap-2">
            <button
                type="button"
                @click="closeForm"
                class="px-4 py-2 border rounded"
            >
              Отмена
            </button>
            <button
                type="submit"
                class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
            >
              Сохранить
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>