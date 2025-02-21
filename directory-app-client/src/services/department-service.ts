import type { Department } from '../domain/Department.ts';
import { BaseService } from './base-service.ts';

export class DepartmentService extends BaseService {
    async getAll(): Promise<Department[]> {
        return this.request<Department[]>('/departments');
    }

    async getParentDepartmentsExcluding(excludeId: number): Promise<Department[]> {
        return this.request<Department[]>(`/departments/parent/${excludeId}`);
    }

    async getChildDepartments(parentId: number): Promise<Department[]> {
        return this.request<Department[]>(`/departments/children/${parentId}`);
    }

    async getById(id: number): Promise<Department> {
        return this.request<Department>(`/departments/${id}`);
    }

    async create(department: Department): Promise<Department> {
        return this.request<Department>('/departments', {
            method: 'POST',
            body: JSON.stringify(department),
        });
    }

    async update(id: number, department: Department): Promise<Department> {
        return this.request<Department>(`/departments/${id}`, {
            method: 'PUT',
            body: JSON.stringify(department),
        });
    }

    async delete(id: number): Promise<void> {
        await this.request(`/departments/${id}`, {
            method: 'DELETE',
        });
    }
}