//user.model.ts

export interface UserDTO {
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
  password?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}


