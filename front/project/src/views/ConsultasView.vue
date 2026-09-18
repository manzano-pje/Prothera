<script setup lang="ts">
import { reactive } from 'vue'
import type { ConsultaState } from '../types/consulta'
import ConsultaResultado from '../components/ConsultaResultado.vue'
import {
  listarTodos,
  listarPorGrupo,
  listarPorAniversario,
  listarMaisVelho,
  listarSomaSalarios,
  listarAlfabetico,
  quantidadeSalarios,
} from '../services/protheraService'

function estadoInicial(): ConsultaState {
  return { loading: false, error: null, tipo: null, dados: null }
}

const resultados = reactive<Record<string, ConsultaState>>({
  listarTodos: estadoInicial(),
  listarPorGrupo: estadoInicial(),
  listarPorAniversario: estadoInicial(),
  listarMaisVelho: estadoInicial(),
  listarSomaSalarios: estadoInicial(),
  listarAlfabetico: estadoInicial(),
  quantidadeSalarios: estadoInicial(),
})

async function executarConsulta(
  chave: string,
  tipo: ConsultaState['tipo'],
  fn: () => Promise<unknown>
) {
  const estado = resultados[chave]
  estado.loading = true
  estado.error = null
  estado.tipo = tipo
  estado.dados = null
  try {
    estado.dados = await fn() as ConsultaState['dados']
  } catch (e) {
    estado.error = e instanceof Error ? e.message : 'Não foi possível obter os dados.'
  } finally {
    estado.loading = false
  }
}
</script>

<template>
  <section class="page">
    <h2 class="page__titulo">Consultas</h2>

    <div class="grid-consultas">
      <!-- 1. Listar todos -->
      <div class="card">
        <div class="card__header">
          <h3 class="card__titulo">Listar todos</h3>
          <button
            class="btn btn--primary"
            :disabled="resultados.listarTodos.loading"
            @click="executarConsulta('listarTodos', 'tabela', listarTodos)"
          >
            {{ resultados.listarTodos.loading ? 'Carregando...' : 'Listar todos' }}
          </button>
        </div>
        <ConsultaResultado :state="resultados.listarTodos" />
      </div>

      <!-- 2. Listar por função -->
      <div class="card">
        <div class="card__header">
          <h3 class="card__titulo">Listar por função</h3>
          <button
            class="btn btn--primary"
            :disabled="resultados.listarPorGrupo.loading"
            @click="executarConsulta('listarPorGrupo', 'grupo', listarPorGrupo)"
          >
            {{ resultados.listarPorGrupo.loading ? 'Carregando...' : 'Listar por função' }}
          </button>
        </div>
        <ConsultaResultado :state="resultados.listarPorGrupo" />
      </div>

      <!-- 3. Aniversariantes -->
      <div class="card">
        <div class="card__header">
          <h3 class="card__titulo">Aniversariantes de outubro e dezembro</h3>
          <button
            class="btn btn--primary"
            :disabled="resultados.listarPorAniversario.loading"
            @click="executarConsulta('listarPorAniversario', 'tabela', listarPorAniversario)"
          >
            {{ resultados.listarPorAniversario.loading ? 'Carregando...' : 'Aniversariantes de outubro e dezembro' }}
          </button>
        </div>
        <ConsultaResultado :state="resultados.listarPorAniversario" />
      </div>

      <!-- 4. Funcionário mais velho -->
      <div class="card">
        <div class="card__header">
          <h3 class="card__titulo">Funcionário mais velho</h3>
          <button
            class="btn btn--primary"
            :disabled="resultados.listarMaisVelho.loading"
            @click="executarConsulta('listarMaisVelho', 'idade', listarMaisVelho)"
          >
            {{ resultados.listarMaisVelho.loading ? 'Carregando...' : 'Funcionário mais velho' }}
          </button>
        </div>
        <ConsultaResultado :state="resultados.listarMaisVelho" />
      </div>

      <!-- 5. Somar salários -->
      <div class="card">
        <div class="card__header">
          <h3 class="card__titulo">Somar salários</h3>
          <button
            class="btn btn--primary"
            :disabled="resultados.listarSomaSalarios.loading"
            @click="executarConsulta('listarSomaSalarios', 'numero', listarSomaSalarios)"
          >
            {{ resultados.listarSomaSalarios.loading ? 'Carregando...' : 'Somar salários' }}
          </button>
        </div>
        <ConsultaResultado :state="resultados.listarSomaSalarios" />
      </div>

      <!-- 6. Ordem alfabética -->
      <div class="card">
        <div class="card__header">
          <h3 class="card__titulo">Ordem alfabética</h3>
          <button
            class="btn btn--primary"
            :disabled="resultados.listarAlfabetico.loading"
            @click="executarConsulta('listarAlfabetico', 'tabela', listarAlfabetico)"
          >
            {{ resultados.listarAlfabetico.loading ? 'Carregando...' : 'Ordem alfabética' }}
          </button>
        </div>
        <ConsultaResultado :state="resultados.listarAlfabetico" />
      </div>

      <!-- 7. Quantidade de salários mínimos -->
      <div class="card">
        <div class="card__header">
          <h3 class="card__titulo">Quantidade de salários mínimos</h3>
          <button
            class="btn btn--primary"
            :disabled="resultados.quantidadeSalarios.loading"
            @click="executarConsulta('quantidadeSalarios', 'salarios-minimos', quantidadeSalarios)"
          >
            {{ resultados.quantidadeSalarios.loading ? 'Carregando...' : 'Quantidade de salários mínimos' }}
          </button>
        </div>
        <ConsultaResultado :state="resultados.quantidadeSalarios" />
      </div>
    </div>
  </section>
</template>

<style scoped>
.page__titulo {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 1.5rem;
}

.grid-consultas {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1.5rem;
}

.card {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  padding: 1.5rem;
}

.card__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.card__titulo {
  font-size: 1.05rem;
  font-weight: 600;
  color: #334155;
  margin: 0;
}

.btn {
  border-radius: 6px;
  padding: 0.55em 1.1em;
  font-size: 0.9rem;
  font-weight: 500;
}

.btn--primary {
  background-color: #2563eb;
  color: #ffffff;
}

.btn--primary:hover:not(:disabled) {
  background-color: #1d4ed8;
}

@media (min-width: 768px) {
  .grid-consultas {
    grid-template-columns: 1fr 1fr;
  }
}

@media (max-width: 640px) {
  .card__header {
    flex-direction: column;
    align-items: stretch;
  }
  .btn {
    width: 100%;
  }
}
</style>
