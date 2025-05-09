import {Dayjs} from "dayjs";
import {ShiftDTO} from "../dto/ShiftDTO.ts";

export interface ShiftFormValues {
    id?: string;
    selectedShift?: ShiftDTO;
    eventDate?: Dayjs;
    duration?: [Dayjs, Dayjs];
    name?: string;
    signups?: string[];
    formName: string;
    min: number;
    max: number;
}