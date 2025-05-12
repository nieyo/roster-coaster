import {ShiftDurationDTO} from "./ShiftDurationDTO.ts";
import {ShiftSignupDTO} from "./ShiftSignupDTO.ts";

export interface ShiftDTO {
    id: string;
    duration: ShiftDurationDTO;
    signups: ShiftSignupDTO[];
    minParticipants: number;
    maxParticipants: number;
}