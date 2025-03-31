export type Shift = {
    id: string;
    startTime: Date;
    endTime: Date;
    volunteers: User[]
}

export type User = {
    id: string;
    name: string;
    email?: string;
    isAnonymous: boolean;
};