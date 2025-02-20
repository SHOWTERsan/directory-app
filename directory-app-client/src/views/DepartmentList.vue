<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { Department } from '../domain/Department.ts';
import { apiClient } from '../logic/api-client.ts';

const departments = ref<Department[]>([])
const parentDepartments = ref<Department[]>([])
const showForm = ref(false)
const editingDepartment = ref<Department | null>(null)
const loading = ref(false)

const newDepartment: Department = {
  name: '',
  notes: '',
  parentId: undefined
}

async function loadDepartments() {
  loading.value = true
  try {
    const [depsResponse, parentDepsResponse] = await Promise.all([
      apiClient.oas['department-controller'].getAllDepartments(),
      apiClient.oas['department-controller'].getParentDepartments()
    ])
    departments.value = depsResponse.body
    parentDepartments.value = parentDepsResponse.body
  } catch (error) {
    console.error('Failed to load departments:', error)
  }
  loading.value = false
}

async function saveDepartment(department: Department) {
  try {
    if (editingDepartment.value?.id) {
      await apiClient.oas['department-controller'].updateDepartment({
        id: editingDepartment.value.id,
        requestBody: department
      })
    } else {
      await apiClient.oas['department-controller'].createDepartment({
        requestBody: department
      })
    }
    await loadDepartments()
    showForm.value = false
    editingDepartment.value = null
  } catch (error) {
    console.error('Failed to save department:', error)
  }
}

async function deleteDepartment(id: number) {
  if (confirm('Вы уверены, что хотите удалить этот отдел?')) {
    try {
      await apiClient.oas['department-controller'].deleteDepartment({ id })
      await loadDepartments()
    } catch (error) {
      console.error('Failed to delete department:', error)
    }
  }
}

function editDepartment(department: Department) {
  editingDepartment.value = { ...department }
  showForm.value = true
}

onMounted(loadDepartments)
</script>

<template>
  <div class="p-4">
    <div class="flex justify-between items-center mb-4">
      <h1 class="text-2xl font-bold">Отделы</h1>
      <button
          @click="showForm = true"
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
      <tr v-for="department in departments" :key="department.id" class="hover:bg-gray-50">
        <td class="px-6 py-4 border-b">
          {{ parentDepartments.find(d => d.id === department.parentId)?.name || '-' }}
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

    <!-- Form Modal -->
    <div v-if="showForm" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div class="bg-white p-6 rounded-lg w-full max-w-md">
        <h2 class="text-xl font-bold mb-4">
          {{ editingDepartment ? 'Изменить отдел' : 'Добавить отдел' }}
        </h2>
        <form @submit.prevent="saveDepartment(editingDepartment || newDepartment)">
          <div class="mb-4">
            <label class="block mb-2">Родительский отдел</label>
            <select
                v-model="(editingDepartment || newDepartment).parentId"
                class="w-full border p-2 rounded"
            >
              <option :value="undefined">-</option>
              <option
                  v-for="parent in parentDepartments"
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
                v-model="(editingDepartment || newDepartment).name"
                required
                class="w-full border p-2 rounded"
            />
          </div>
          <div class="mb-4">
            <label class="block mb-2">Примечание</label>
            <textarea
                v-model="(editingDepartment || newDepartment).notes"
                class="w-full border p-2 rounded"
            />
          </div>
          <div class="flex justify-end gap-2">
            <button
                type="button"
                @click="showForm = false"
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