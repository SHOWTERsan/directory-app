import { BaseService } from './base-service.ts';
import type { Employee } from '../domain/Employee.ts';

export class EmployeeService extends BaseService {
    async getAll(): Promise<Employee[]> {
        return this.request<Employee[]>('/employees');
    }

    async getById(id: number): Promise<Employee> {
        return this.request<Employee>(`/employees/${id}`);
    }

    async getByDepartment(departmentId: number): Promise<Employee[]> {
        return this.request<Employee[]>(`/employees/department/${departmentId}`);
    }

    async getByProfession(professionId: number): Promise<Employee[]> {
        return this.request<Employee[]>(`/employees/profession/${professionId}`);
    }

    async create(employee: Employee): Promise<Employee> {
        return this.request<Employee>('/employees', {
            method: 'POST',
            body: JSON.stringify(employee),
        });
    }

    async update(id: number, employee: Employee): Promise<Employee> {
        return this.request<Employee>(`/employees/${id}`, {
            method: 'PUT',
            body: JSON.stringify(employee),
        });
    }

    async delete(id: number): Promise<void> {
        await this.request(`/employees/${id}`, {
            method: 'DELETE',
        });
    }
}