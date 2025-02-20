export interface Department {
    id?: number
    name: string
    notes?: string
    parentId?: number
    children?: Department[]
}