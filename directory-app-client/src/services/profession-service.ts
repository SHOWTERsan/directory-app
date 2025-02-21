import { BaseService } from './base-service.ts';
import type { Profession } from '../domain/Profession.ts';

export class ProfessionService extends BaseService {
    async getAll(): Promise<Profession[]> {
        return this.request<Profession[]>('/professions');
    }

    async getById(id: number): Promise<Profession> {
        return this.request<Profession>(`/professions/${id}`);
    }

    async create(profession: Profession): Promise<Profession> {
        return this.request<Profession>('/professions', {
            method: 'POST',
            body: JSON.stringify(profession),
        });
    }

    async update(id: number, profession: Profession): Promise<Profession> {
        return this.request<Profession>(`/professions/${id}`, {
            method: 'PUT',
            body: JSON.stringify(profession),
        });
    }

    async delete(id: number): Promise<void> {
        await this.request(`/professions/${id}`, {
            method: 'DELETE',
        });
    }
}