<script setup lang="ts">
import {onMounted, ref} from 'vue'
import type {Profession} from '../domain/Profession.ts'
import {ProfessionService} from '../services/profession-service.ts'

const professionService = new ProfessionService()

const professions = ref<Profession[]>([])
const showForm = ref(false)
const editingProfession = ref<Profession | null>(null)
const loading = ref(false)

function createNewProfession(): Profession {
  return {
    name: '',
    notes: ''
  }
}

const formProfession = ref<Profession>(createNewProfession())

async function loadProfessions() {
  loading.value = true
  try {
    professions.value = await professionService.getAll()
  } catch (error) {
    console.error('Failed to load professions:', error)
  }
  loading.value = false
}

async function saveProfession(profession: Profession) {
  try {
    if (editingProfession.value?.id) {
      await professionService.update(editingProfession.value.id, profession)
    } else {
      await professionService.create(profession)
    }
    await loadProfessions()
    closeForm()
  } catch (error) {
    console.error('Failed to save profession:', error)
  }
}

function closeForm() {
  showForm.value = false
  editingProfession.value = null
  formProfession.value = createNewProfession()
}

async function deleteProfession(id: number) {
  if (confirm('Вы уверены, что хотите удалить эту профессию?')) {
    try {
      await professionService.delete(id)
      await loadProfessions()
    } catch (error) {
      console.error('Failed to delete profession:', error)
    }
  }
}

function editProfession(profession: Profession) {
  editingProfession.value = { ...profession }
  formProfession.value = { ...profession }
  showForm.value = true
}

function showAddForm() {
  formProfession.value = createNewProfession()
  editingProfession.value = null
  showForm.value = true
}

onMounted(loadProfessions)
</script>

<template>
  <div class="p-4">
    <div class="flex justify-between items-center mb-4">
      <h1 class="text-2xl font-bold">Профессии</h1>
      <button
          @click="showAddForm"
          class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
      >
        Добавить профессию
      </button>
    </div>

    <div v-if="loading" class="text-center py-4">Загрузка...</div>

    <table v-else class="min-w-full bg-white border">
      <thead>
      <tr>
        <th class="px-6 py-3 border-b text-left">Наименование</th>
        <th class="px-6 py-3 border-b text-left">Примечание</th>
        <th class="px-6 py-3 border-b text-right">Действия</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="profession in professions" :key="profession.id" class="hover:bg-gray-50">
        <td class="px-6 py-4 border-b">{{ profession.name }}</td>
        <td class="px-6 py-4 border-b">{{ profession.notes }}</td>
        <td class="px-6 py-4 border-b text-right">
          <button
              @click="editProfession(profession)"
              class="text-blue-500 hover:text-blue-700 mr-2"
          >
            Изменить
          </button>
          <button
              @click="deleteProfession(profession.id!)"
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
          {{ editingProfession ? 'Изменить профессию' : 'Добавить профессию' }}
        </h2>
        <form @submit.prevent="saveProfession(formProfession)">
          <div class="mb-4">
            <label class="block mb-2">Наименование</label>
            <input
                v-model="formProfession.name"
                required
                class="w-full border p-2 rounded"
            />
          </div>
          <div class="mb-4">
            <label class="block mb-2">Примечание</label>
            <textarea
                v-model="formProfession.notes"
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