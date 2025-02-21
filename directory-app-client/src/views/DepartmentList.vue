<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { Department } from '../domain/Department.ts'
import { DepartmentService } from '../services/department-service.ts'

const departmentService = new DepartmentService()

const departments = ref<Department[]>([])
const formParentDepartments = ref<Department[]>([])

const showForm = ref(false)
const editingDepartment = ref<Department | null>(null)
const loading = ref(false)

function createNewDepartment(): Department {
  return {
    name: '',
    notes: '',
    parentId: undefined
  }
}

const formDepartment = ref<Department>(createNewDepartment())

async function loadDepartments() {
  loading.value = true
  try {
    departments.value = await departmentService.getAll()
  } catch (error) {
    console.error('Failed to load departments:', error)
  }
  loading.value = false
}

async function saveDepartment(department: Department) {
  try {
    if (editingDepartment.value?.id) {
      await departmentService.update(editingDepartment.value.id, department)
    } else {
      await departmentService.create(department)
    }
    await loadDepartments()
    closeForm()
  } catch (error) {
    console.error('Failed to save department:', error)
  }
}

function closeForm() {
  showForm.value = false
  editingDepartment.value = null
  formDepartment.value = createNewDepartment()
  formParentDepartments.value = departments.value
}

async function deleteDepartment(id: number) {
  if (confirm('Вы уверены, что хотите удалить этот отдел?')) {
    try {
      await departmentService.delete(id)
      await loadDepartments()
    } catch (error) {
      console.error('Failed to delete department:', error)
    }
  }
}

async function editDepartment(department: Department) {
  editingDepartment.value = { ...department }
  formDepartment.value = { ...department }
  try {
    formParentDepartments.value = await departmentService.getParentDepartmentsExcluding(
        department.id!
    )
  } catch (error) {
    console.error('Failed to load form parent departments:', error)
  }
  showForm.value = true
}

function showAddForm() {
  formDepartment.value = createNewDepartment()
  editingDepartment.value = null
  formParentDepartments.value = departments.value
  showForm.value = true
}

onMounted(loadDepartments)
</script>

<template>
  <div class="p-4">
    <div class="flex justify-between items-center mb-4">
      <h1 class="text-2xl font-bold">Отделы</h1>
      <button
          @click="showAddForm"
          class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
      >
        Добавить отдел
      </button>
    </div>

    <div v-if="loading" class="text-center py-4">Загрузка...</div>

    <table v-else class="min-w-full bg-white border">
      <thead>
      <tr>
        <th class="px-6 py-3 border-b text-left">Родительский отдел</th>
        <th class="px-6 py-3 border-b text-left">Наименование</th>
        <th class="px-6 py-3 border-b text-left">Примечание</th>
        <th class="px-6 py-3 border-b text-right">Действия</th>
      </tr>
      </thead>
      <tbody>
      <tr
          v-for="department in departments"
          :key="department.id"
          class="hover:bg-gray-50"
      >
        <td class="px-6 py-4 border-b">
          {{ departments.find(p => p.id === department.parentId)?.name || '-' }}
        </td>
        <td class="px-6 py-4 border-b">{{ department.name }}</td>
        <td class="px-6 py-4 border-b">{{ department.notes }}</td>
        <td class="px-6 py-4 border-b text-right">
          <button
              @click="editDepartment(department)"
              class="text-blue-500 hover:text-blue-700 mr-2"
          >
            Изменить
          </button>
          <button
              @click="deleteDepartment(department.id!)"
              class="text-red-500 hover:text-red-700"
          >
            Удалить
          </button>
        </td>
      </tr>
      </tbody>
    </table>

    <div
        v-if="showForm"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center"
    >
      <div class="bg-white p-6 rounded-lg w-full max-w-md">
        <h2 class="text-xl font-bold mb-4">
          {{ editingDepartment ? 'Изменить отдел' : 'Добавить отдел' }}
        </h2>
        <form @submit.prevent="saveDepartment(formDepartment)">
          <div class="mb-4">
            <label class="block mb-2">Родительский отдел</label>
            <select v-model="formDepartment.parentId" class="w-full border p-2 rounded">
              <option :value="undefined">-</option>
              <option
                  v-for="parent in formParentDepartments"
                  :key="parent.id"
                  :value="parent.id"
              >
                {{ parent.name }}
              </option>
            </select>
          </div>
          <div class="mb-4">
            <label class="block mb-2">Наименование</label>
            <input
                v-model="formDepartment.name"
                required
                class="w-full border p-2 rounded"
            />
          </div>
          <div class="mb-4">
            <label class="block mb-2">Примечание</label>
            <textarea
                v-model="formDepartment.notes"
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
