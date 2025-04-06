//user.model.ts

export interface UserDTO {
  id: number;
  username: string;
  email: string;
  password?: string;
  dateOfBirth: Date;
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  address?: string;
  roles?: string[];
  banned?: boolean;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  passwordConfirm: string;
  dateOfBirth: string;
}

