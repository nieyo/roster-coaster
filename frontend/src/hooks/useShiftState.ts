import {useState, useEffect} from 'react';
import axios from 'axios';
import {Shift} from "../types/types.ts";

const useShiftState = () => {
    const [shifts, setShifts] = useState<Shift[]>([]);
    const [shiftsIsLoading, setShiftsIsLoading] = useState<boolean>(true);
    const [shiftsError, setShiftsError] = useState<string | null>(null);

    const baseURL = "/api/shift";

    const getShifts = async () => {
        try {
            setShiftsIsLoading(true);
            const response = await axios.get(baseURL);
            setShifts(convertShift(response.data));
            setShiftsError(null);
        } catch (err) {
            setShiftsError('Failed to fetch shifts');
            console.error(err);
        } finally {
            setShiftsIsLoading(false);
        }
    };

    const convertShift = (rawShifts: Shift[]): Shift[] => {
        return rawShifts.map(shift => {
            shift.startTime = new Date(shift.startTime)
            shift.endTime = new Date(shift.endTime)
            return shift
        })
    }

    // TODO: refactor and fix whatever the problem is
    useEffect(() => {
        getShifts()
    }, [])

    return {shifts, shiftsIsLoading, shiftsError, getShifts};
};

export default useShiftState;
