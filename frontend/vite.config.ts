import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // Forward GraphQL requests to Spring Boot so the browser sees a single origin (no CORS setup needed).
      '/graphql': 'http://localhost:8080',
    },
  },
})
