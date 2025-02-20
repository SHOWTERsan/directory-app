<template>
  <div id="app">
    <nav class="bg-gray-800 p-4">
      <div class="container mx-auto flex items-center justify-between">
        <div class="text-white font-bold text-xl">Справочник</div>
        <ul class="flex space-x-4 list-none">
          <li>
            <router-link to="/employees" class="text-gray-300 hover:text-white">Сотрудники</router-link>
          </li>
          <li>
            <router-link to="/departments" class="text-gray-300 hover:text-white">Отделы</router-link>
          </li>
          <li>
            <router-link to="/professions" class="text-gray-300 hover:text-white">Профессии</router-link>
          </li>
        </ul>
      </div>
    </nav>
    <main class="container mx-auto p-4">
      <router-view />
    </main>
  </div>
</template>