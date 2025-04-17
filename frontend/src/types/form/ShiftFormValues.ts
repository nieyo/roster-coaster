import dayjs from "dayjs";
import {ShiftDTO} from "../dto/ShiftDTO.ts";

export interface ShiftFormValues {
    id?: string;
    selectedShift?: ShiftDTO;
    tomorrow?: dayjs.Dayjs;
    duration?: [dayjs.Dayjs, dayjs.Dayjs];
    name?: string;
    participants?: string[];
    formName: string;
}