import {Dayjs} from "dayjs";
import {ShiftDTO} from "../dto/ShiftDTO.ts";

export interface ShiftFormValues {
    id?: string;
    selectedShift?: ShiftDTO;
    tomorrow?: Dayjs;
    duration?: [Dayjs, Dayjs];
    name?: string;
    participants?: string[];
    formName: string;
}