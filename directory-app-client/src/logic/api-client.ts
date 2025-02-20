import SwaggerClient from 'swagger-client'

class ApiClient {
    oas: any

    async initialize(): Promise<void> {
        if (this.oas) return

        const client = await new SwaggerClient({
            url: 'api/v3/api-docs',
        });

        this.oas = client.apis;
    }
}

export const apiClient = new ApiClient()