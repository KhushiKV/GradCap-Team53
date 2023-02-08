import axios from 'axios';
import React from 'react'
import { Doughnut } from 'react-chartjs-2';

export default function Dashboard() {

    const [dashboard, setDashboard] = React.useState({
        users: 0,
        devices: 0,
        alerts: 0
    })
    const [loading, setLoading] = React.useState(true)

    React.useEffect(() => {
        const token = localStorage.getItem('token')
        axios.get(`${process.env.REACT_APP_API_URL}/dashboard`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        }).then(res => {
            console.log(res.data)
            setDashboard(res.data)
            setLoading(false)
        }
        ).catch(err => {
            console.log(err.response.data)
            setLoading(false)
        })
    }, [])

    const data1 = {
        labels: [dashboard.users],
        datasets: [
            {
                label: 'No. of Users',
                data: [dashboard.users, 10],
                backgroundColor: [
                    'blue',
                    'grey',
                ],
                pointBackgroundColor: 'blue',
            }

        ]
    }
    const data2 = {
        labels: [dashboard.devices],
        datasets: [
            {
                label: 'No. of Devices',
                data: [dashboard.devices, 10],
                backgroundColor: [
                    'green',
                    'grey',
                ],
                pointBackgroundColor: 'blue',
            }

        ]
    }
    const data3 = {
        labels: [dashboard.alerts],
        datasets: [
            {
                label: 'No. of Accidents Reported',
                data: [dashboard.alerts, 30],
                backgroundColor: [
                    'red',
                    'grey',
                ],
                pointBackgroundColor: 'blue',
            }

        ]
    }

    const options1 = {
        plugins: {
            title: {
                display: true,
                text: 'Total Registered Users',
                color: 'blue',
                font: {
                    size: 20
                },
                padding: {
                    top: 30,
                    bottom: 30
                },
                responsive: true,
                animation: {
                    animateScale: true,
                }
            }
        }
    }
    const options2 = {
        plugins: {
            title: {
                display: true,
                text: 'Total Registered Devices',
                color: 'green',
                font: {
                    size: 20
                },
                padding: {
                    top: 30,
                    bottom: 30
                },
                responsive: true,
                animation: {
                    animateScale: true,
                }
            }
        }
    }
    const options3 = {
        plugins: {
            title: {
                display: true,
                text: 'Total Accidents Reported',
                color: 'red',
                font: {
                    size: 20
                },
                padding: {
                    top: 30,
                    bottom: 30
                },
                responsive: true,
                animation: {
                    animateScale: true,
                }
            }
        }
    }

    return (
        <>
            <div className='p-3 h5 text-secondary'>
                Dashboard
            </div>
            {
                loading ?
                    <div className="text-center mt-5">
                        <div className="spinner-border text-primary me-2" role="status">
                        </div>
                        <span className="sr-only text-primary fs-3 fw-bold">Loading...</span>
                    </div> :
                    < div className="row">
                        <div className="col-4" >
                            <Doughnut data={data1} options={options1} />
                        </div>
                        <div className="col-4">
                            <Doughnut data={data2} options={options2} />
                        </div>
                        <div className="col-4">
                            <Doughnut data={data3} options={options3} />
                        </div>
                    </div>
            }
        </>
    )
}
