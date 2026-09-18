import axios from 'axios'

const baseURL = import.meta.env.VITE_API_URL || 'https://prothera.onrender.com'

export const api = axios.create({
  baseURL: `${baseURL}/api/v1/prothera`,
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const message =
      error.response?.data?.message ||
      error.response?.data?.error ||
      'Não foi possível concluir a operação. Verifique sua conexão e tente novamente.'
    return Promise.reject(new Error(message))
  }
)
