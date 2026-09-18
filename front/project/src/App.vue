<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="app-shell">
    <header class="app-header">
      <div class="app-header__inner">
        <h1 class="app-title">Prothera — Gestão de Funcionários</h1>
        <nav class="app-nav">
          <RouterLink to="/funcionarios" class="nav-link">Funcionários</RouterLink>
          <RouterLink to="/consultas" class="nav-link">Consultas</RouterLink>
        </nav>
      </div>
    </header>
    <main class="app-main">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.app-shell {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  background: #1e293b;
  color: #f1f5f9;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.app-header__inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem 1.5rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.app-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
}

.app-nav {
  display: flex;
  gap: 0.5rem;
}

.nav-link {
  padding: 0.5rem 1rem;
  border-radius: 6px;
  color: #cbd5e1;
  text-decoration: none;
  font-weight: 500;
  font-size: 0.95rem;
  transition: background-color 0.2s, color 0.2s;
}

.nav-link:hover {
  background-color: #334155;
  color: #f8fafc;
}

.nav-link.router-link-exact-active {
  background-color: #2563eb;
  color: #ffffff;
}

.app-main {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 2rem 1.5rem;
}

@media (max-width: 640px) {
  .app-header__inner {
    flex-direction: column;
    align-items: stretch;
  }
  .app-nav {
    justify-content: center;
  }
}
</style>
