import axios from 'axios'
import { Button, Modal } from 'react-bootstrap'
import React from 'react'
import DeleteBtn from './DeleteBtn'

export default function Devices() {

    const [devices, setDevices] = React.useState([])
    const [loading, setLoading] = React.useState(true)
    const [rmn, setRMN] = React.useState('')
    const [unique_id, setUniqueId] = React.useState('')
    const [name, setName] = React.useState('')
    const [model, setModel] = React.useState('')
    const [show, setShow] = React.useState(false)


    React.useEffect(() => {
        const token = localStorage.getItem('token')
        axios.get(`${process.env.REACT_APP_API_URL}/devices`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        }).then(res => {
            console.log(res.data)
            setDevices(res.data)
            setLoading(false)
        }
        ).catch(err => {
            console.log(err.response.data)
            setLoading(false)
        })
    }, [])

    const createDevice = (e) => {
        e.preventDefault()
        const token = localStorage.getItem('token')

        const data = {
            rmn: rmn,
            unique_id: unique_id,
            name: name,
            model: model
        }

        console.log(data);

        axios.post('http://localhost:5000/devices', data, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        }).then(res => {
            console.log(res.data)
            setDevices([...devices, res.data.doc])
            setShow(false)
        }
        ).catch(err => {
            console.log(err.response.data)
        })
    }


    return (
        <>
            <div className="d-flex">
                <div className='p-3 h5 text-secondary'>Devices</div>
                <div className="p-3">
                    <Button variant="primary" size='sm' onClick={() => setShow(true)}>Create Device</Button>
                </div>
            </div>

            {
                loading ? <div className="text-center mt-5">
                    <div className="spinner-border text-primary me-2" role="status">
                    </div>
                    <span className="sr-only text-primary fs-3 fw-bold">Loading...</span>
                </div> : devices.length === 0 ? <div className="text-center mt-5">
                    <span className="sr-only text-secondary">No Devices Found</span>
                </div> :
                    <div className="ul">
                        {
                            devices.map(device => {
                                return (
                                    <div className="li" key={device.id}>
                                        <div className="card my-2">
                                            <div className="card-body">
                                                <div className="row">
                                                    <div className="col">
                                                        <div className="card-title">Unique Id: {device.unique_id}</div>
                                                        <p className="card-text">RMN: {device.rmn}</p>
                                                        <div className="card-subtitle mb-2 text-muted">
                                                            User Id: {device.user_id ? device.user_id : "Not Assigned"}
                                                        </div>

                                                    </div>
                                                    <div className="col">
                                                        <p className="card-text">Name: {device.name}</p>
                                                        <p className="card-text">Model: {device.model}</p>
                                                        <p className='text-muted'> location:  {device.location && device.location}</p>
                                                    </div>
                                                    <div className='col'>
                                                        <div className="d-flex flex-column align-items-end">
                                                            <div className="w-25">
                                                                <DeleteBtn device={device} setDevices={setDevices} devices={devices} />
                                                            </div>
                                                        </div>

                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                )
                            })
                        }
                    </div>
            }
            <Modal show={show} onHide={() => setShow(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Add Device</Modal.Title>
                </Modal.Header>
                <form onSubmit={createDevice}>
                    <Modal.Body>
                        <div className="mb-3">
                            <label htmlFor="rmn" className="form-label">RMN</label>
                            <input type="mobile" className="form-control" id="rmn" onChange={(e) => setRMN(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="device_id" className="form-label">Device Unique ID</label>
                            <input type="text" className="form-control" id="device_id" onChange={(e) => setUniqueId(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="device_name" className="form-label">Device Name</label>
                            <input type="text" className="form-control" id="device_name" onChange={(e) => setName(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="device_model" className="form-label">Device Model</label>
                            <input type="text" className="form-control" id="device_model" onChange={(e) => setModel(e.target.value)} />
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShow(false)}>
                            Close
                        </Button>
                        <Button type='submit' variant="primary" onClick={() => setShow(false)}>
                            Create
                        </Button>
                    </Modal.Footer>
                </form>

            </Modal>
        </>
    )
}
