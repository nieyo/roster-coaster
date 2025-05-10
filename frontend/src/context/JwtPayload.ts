export type JwtPayload = {
    sub: string;      // email
    id: number;       // user id
    name: string;     // username
    iat: number;      // issued at (timestamp)
    exp: number;      // expiration (timestamp)
};