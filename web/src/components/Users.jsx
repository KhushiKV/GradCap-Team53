import axios from 'axios'
import React from 'react'

export default function Users() {

    const [users, setUsers] = React.useState([])
    const [loading, setLoading] = React.useState(true)

    React.useEffect(() => {
        const token = localStorage.getItem('token')
        axios.get(`${process.env.REACT_APP_API_URL}/users`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        }).then(res => {
            console.log(res.data)
            setUsers(res.data)
            setLoading(false)
        }
        ).catch(err => {
            console.log(err.response.data)
            setLoading(false)
        })
    }, [])

    return (
        <>
            <div className='p-3 h5 text-secondary'>Users</div>
            {
                loading ? <div className="text-center mt-5">
                    <div className="spinner-border text-primary me-2" role="status">
                    </div>
                    <span className="sr-only text-primary fs-3 fw-bold">Loading...</span>
                </div> :
                    users.length === 0 ? <div className="text-center mt-5">
                        <span className="sr-only text-secondary">No Feedbacks</span>
                    </div>
                        :
                        <div className="ul">
                            {
                                users.map(user => {
                                    return (
                                        <div className="li my-2" key={user.id}>
                                            <div className="card">
                                                <div className="card-body">
                                                    <h5 className="card-title">{user.name}</h5>
                                                    <h6 className="card-subtitle mb-2 text-muted">{user.email}</h6>
                                                    <p className="card-text">{user.role}</p>

                                                </div>
                                            </div>
                                        </div>
                                    )
                                })

                            }
                        </div>
            }

        </>
    )
}
