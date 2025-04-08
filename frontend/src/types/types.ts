import dayjs from "dayjs";

export type Shift = {
    id: string;
    startTime: Date;
    endTime: Date;
    participants: User[]
}

export type User = {
    name: string;
};

export interface ShiftFormValues {
    id?: string
    selectedShift?: Shift
    tomorrow?: dayjs.Dayjs
    duration?: [dayjs.Dayjs, dayjs.Dayjs]
    name?: string
    participants?: string[]
    formName: string
}