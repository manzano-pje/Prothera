<script setup lang="ts">
defineProps<{
  open: boolean
  message: string
  confirmText?: string
  cancelText?: string
  loading?: boolean
}>()

const emit = defineEmits<{
  confirm: []
  cancel: []
}>()
</script>

<template>
  <div v-if="open" class="dialog-overlay" @click.self="emit('cancel')">
    <div class="dialog" role="dialog" aria-modal="true">
      <p class="dialog__message">{{ message }}</p>
      <div class="dialog__actions">
        <button class="btn btn--secondary" :disabled="loading" @click="emit('cancel')">
          {{ cancelText || 'Cancelar' }}
        </button>
        <button class="btn btn--danger" :disabled="loading" @click="emit('confirm')">
          {{ loading ? 'Excluindo...' : (confirmText || 'Confirmar') }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dialog-overlay {
  position: fixed;
  inset: 0;
  background-color: rgba(15, 23, 42, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
  padding: 1rem;
}

.dialog {
  background: #ffffff;
  border-radius: 10px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  padding: 1.75rem;
  max-width: 420px;
  width: 100%;
}

.dialog__message {
  font-size: 1.05rem;
  color: #1e293b;
  margin: 0 0 1.5rem;
  line-height: 1.5;
}

.dialog__actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
}

.btn {
  border-radius: 6px;
  padding: 0.6em 1.2em;
  font-size: 0.95rem;
  font-weight: 500;
}

.btn--secondary {
  background-color: #f1f5f9;
  color: #475569;
  border: 1px solid #cbd5e1;
}

.btn--secondary:hover:not(:disabled) {
  background-color: #e2e8f0;
}

.btn--danger {
  background-color: #dc2626;
  color: #ffffff;
}

.btn--danger:hover:not(:disabled) {
  background-color: #b91c1c;
}
</style>
