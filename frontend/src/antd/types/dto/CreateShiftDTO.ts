import {ShiftDurationDTO} from "./ShiftDurationDTO.ts";
import {ShiftSignupDTO} from "./ShiftSignupDTO.ts";

export interface CreateShiftDTO {
    duration: ShiftDurationDTO;
    signups: ShiftSignupDTO[];
    minParticipants: number;
    maxParticipants: number;
}