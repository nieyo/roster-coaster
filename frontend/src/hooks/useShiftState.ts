import {useCallback, useEffect, useState} from 'react';
import axios from 'axios';
import {CreateShiftDTO} from "../types/dto/CreateShiftDTO.ts";
import {ShiftDTO} from "../types/dto/ShiftDTO.ts";



const useShiftState = () => {
    const [shiftList, setShiftList] = useState<ShiftDTO[]>([]);
    const [shiftListIsLoading, setShiftListIsLoading] = useState<boolean>(true);
    const [shiftListError, setShiftListError] = useState<string | null>(null);
    const baseURL = "/api/shift";

    const getShiftList = useCallback(() => {
        setShiftListIsLoading(true);
        axios.get(baseURL)
            .then((response) => {
                setShiftList(response.data);
                setShiftListError(null);
            })
            .catch(() => setShiftListError('Failed to fetch shifts'))
            .finally(() => setShiftListIsLoading(false));
    }, []);

    const deleteShift = (id: string) => {
        setShiftListIsLoading(true)
        axios.delete(`${baseURL}/${id}`)
            .then(() => {
                getShiftList()
            })
            .catch((error) => {
                console.error("Error deleting Shift:", error);
                alert("Failed to delete. Please try again.");
            })
            .finally(() =>
                setShiftListIsLoading(false)
            )
    };

    const updateShift = (id: string, shiftToUpdate: ShiftDTO) => {
        setShiftListIsLoading(true)
        axios.put(`${baseURL}/${id}`, shiftToUpdate)
            .then(() => {
                getShiftList()
            })
            .catch((error) => {
                setShiftListError(error.message);
            })
            .finally(() => {
                setShiftListIsLoading(false)
            })
    };

    const saveShift = (shiftToCreate: CreateShiftDTO) => {
        setShiftListIsLoading(true)
        axios.post(baseURL, shiftToCreate)
            .then(() => {
                getShiftList()
            })
            .catch((error) => {
                setShiftListError(error.message);
            })
            .finally(() => {
                setShiftListIsLoading(false)
            })
    };

    useEffect(() => {
        getShiftList();
    }, [getShiftList]);

    return {shiftList, shiftListIsLoading, shiftListError, getShiftList, deleteShift, updateShift, saveShift};
}

export default useShiftState;
