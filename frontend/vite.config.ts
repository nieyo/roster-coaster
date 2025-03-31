import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
    plugins: [react()],
    optimizeDeps: {
        exclude: ['@tabler/icons-react']
    },
    server: {
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
            }
        }
    }
})
