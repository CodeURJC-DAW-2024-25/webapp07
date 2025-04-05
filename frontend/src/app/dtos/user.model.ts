//user.model.ts

export interface User {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  dateOfBirth: Date;
  phoneNumber: string;
  address: string;
  email: string;
  roles: string[];
  banned: boolean;
  
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  status: 'SUCCESS' | 'ERROR';
  message?: string;
  user?: User;
  token?: string;
}
