import React from 'react'
import { useNavigate } from 'react-router-dom'
import Dashboard from './components/Dashboard'
import Users from './components/Users'
import Devices from './components/Devices'
import Feeadback from './components/Feedback'


export default function DashboardPage({ auth, setAuth, loading }) {

    const navigate = useNavigate()
    const [tab, setTab] = React.useState('dashboard')

    return (
        <div className="Dashboard">
            <div className="container-fluid p-4">
                {
                    loading ?
                        <div className="text-center mt-5">
                            <div className="spinner-border text-primary me-2" role="status">
                            </div>
                            <span className="sr-only text-primary fs-3 fw-bold">Loading...</span>
                        </div>
                        : auth ?
                            <>
                                <div className="row mt-3">
                                    <div className="col-2">
                                        <div className="ul p-4 card">
                                            <li className='my-2'>
                                                <span
                                                    role={'button'}
                                                    style={{
                                                        color:
                                                            tab === 'dashboard' ? 'black' : 'blue',
                                                        textDecoration: tab === 'dashboard' ? 'underline' : 'none',
                                                    }}
                                                    onClick={() => setTab('dashboard')}> Dashboard</span>
                                            </li>
                                            <li className='my-2'>
                                                <span
                                                    role={'button'}
                                                    style={{
                                                        color:
                                                            tab === 'users' ? 'black' : 'blue',
                                                        textDecoration: tab === 'users' ? 'underline' : 'none',
                                                    }}
                                                    onClick={() => setTab('users')}> Users</span>
                                            </li>
                                            <li className='my-2'>
                                                <span
                                                    role={'button'}
                                                    style={{
                                                        color:
                                                            tab === 'devices' ? 'black' : 'blue',
                                                        textDecoration: tab === 'devices' ? 'underline' : 'none',
                                                    }}
                                                    onClick={() => setTab('devices')}> Devices</span>
                                            </li>
                                            <li className='my-2'>
                                                <span
                                                    role={'button'}
                                                    style={{
                                                        color:
                                                            tab === 'feedback' ? 'black' : 'blue',
                                                        textDecoration: tab === 'feedback' ? 'underline' : 'none',
                                                    }}
                                                    onClick={() => setTab('feedback')}> Feedback</span>
                                            </li>
                                            {/* <li className='my-2'>
                                                <Link onClick={() => setTab('settings')}> Settings</Link>
                                            </li> */}
                                            <br />
                                            <div className='btn btn-danger btn-sm' onClick={() => {
                                                localStorage.removeItem('token')
                                                setAuth(false)
                                                navigate('/login')
                                            }} > Logout</div>

                                        </div>
                                    </div>
                                    <div className="col-10 card" style={{ height: 540, overflowY: 'scroll' }}>
                                        {
                                            tab === 'dashboard' ?
                                                <Dashboard /> : tab === 'users' ?
                                                    <Users /> : tab === 'devices' ?
                                                        <Devices /> : tab === 'feedback' ?
                                                            <Feeadback /> : null

                                        }
                                    </div>
                                </div>
                            </> : null
                }
            </div>
        </div>
    )
}
