import axios from 'axios'
import React from 'react'
import { Button } from 'react-bootstrap'

export default function DeleteBtn({ setDevices, devices, device }) {

    const [deleting, setDeleting] = React.useState(false)

    const deleteDevice = (e) => {
        setDeleting(true)
        const token = localStorage.getItem('token')
        axios.delete(`${process.env.REACT_APP_API_URL}/devices/${device.id}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        }).then(res => {
            console.log(res.data)
            setDeleting(false)
            setDevices(devices.filter(d => d.id !== device.id))
        }
        ).catch(err => {
            setDeleting(false)
            console.log(err)
            alert(err.message)
        })
    }

    return (
        <div>
            <Button disabled={deleting} variant="danger" size='sm' onClick={deleteDevice}>{deleting ? "Deleting..." : "Delete"}</Button>
        </div>
    )
}
