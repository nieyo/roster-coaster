export type Shift = {
    id: string;
    startTime: Date;
    endTime: Date;
    participants: User[]
}

export type User = {
    name: string;
};