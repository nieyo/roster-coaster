import {useState, useEffect} from 'react';
import axios from 'axios';
import {Shift} from "../types/types.ts";

const useShiftState = () => {
    const [shifts, setShifts] = useState<Shift[]>([]);
    const [shiftsIsLoading, setLoading] = useState<boolean>(true);
    const [shiftsError, setError] = useState<string | null>(null);

    const baseURL = "/api/shift";

    const getShifts = async () => {
        try {
            setLoading(true);
            const response = await axios.get(baseURL);
            setShifts(convertShift(response.data));
            setError(null);
        } catch (err) {
            setError('Failed to fetch shifts');
            console.error(err);
        } finally {
            setLoading(false);
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
