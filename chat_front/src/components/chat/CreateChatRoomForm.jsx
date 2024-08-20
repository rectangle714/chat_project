import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, TextField } from '@mui/material';
import Layout from '@layout/Layout';
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import api from '@stores/api';


const CreateChatRoomForm = () => {
    const navigate = useNavigate();
    const [roomName, setRoomName] = useState('');
    const [numberPeople, setNumberPeople] = useState('');
    const { id } = useParams();

    /* 방 정보 생성 or 변경 */
    const createRoomBtn = async() => {
        if(roomName === '') { 
            alert('방 이름을 입력해주세요.');
            return false;
        }
        if(numberPeople === '') {
            alert('인원 수를 선택해주세요.');
            return false;
        }

        if(id) {
            try {
                await api.post('/api/chatRoom/update',null ,{
                    params : {
                        id: id,
                        roomName : roomName,
                        numberPeople : numberPeople
                    }
                });
                alert('채팅방 정보가 변경 되었습니다.');
                navigate('/chatRoom');
            } catch(e) {
                alert('채팅방 정보가 변경에 실패했습니다.');
                console.log(e);
            }
        } else {
            try {
                await api.post('/api/chatRoom/add',null ,{
                    params : {
                        roomName : roomName,
                        numberPeople : numberPeople
                    }
                });
                alert('채팅방이 생성 되었습니다.');
                navigate('/chatRoom');
            } catch(e) {
                alert('채팅방 생성에 실패했습니다.');
                console.log(e);
            }
        }
    }

    /* 채팅방 수정일 때 채팅방 정보 조회 */
    const getRoomInfo = async () => {
        try {
            const response = await api.get(`/api/chatRoom/${id}`);
            setRoomName(response.data.roomName);
            setNumberPeople(response.data.numberPeople);
            console.log('response: ',response);
        } catch(error) {
            console.log('채팅방 정보 error: ', error);
        }
        
    }

    useEffect(() => {
        if(id) {
            getRoomInfo();
        }
    }, [])

    return (
        <Layout>
            <div className="chat-room-wrap">
                <div className="chat-room-list">
                    <div className="chat-room-header">
                        {id ? <h2>채팅방 수정</h2> : <h2>채팅방 생성</h2>}
                    </div>
                    <div style={{textAlign:'right', paddingBottom:'20px', textAlign:'center'}}>
                        <TextField sx={{padding:'0px'}} variant="standard" label="방 이름" onChange={(e) => setRoomName(e.target.value)} value={roomName} />
                    </div>
                    <div style={{width:'200px', margin:'0 auto'}}>
                        <Box>
                            <FormControl sx={{textAlign: 'center'}} fullWidth>
                                <InputLabel id="demo-simple-select-label">인원 수</InputLabel>
                                <Select
                                id="demo-simple-select"
                                value={numberPeople}
                                label="Age"
                                onChange={(e) => setNumberPeople(e.target.value)}
                                >
                                <MenuItem value={1}>1</MenuItem>
                                <MenuItem value={2}>2</MenuItem>
                                <MenuItem value={3}>3</MenuItem>
                                <MenuItem value={4}>4</MenuItem>
                                <MenuItem value={5}>5</MenuItem>
                                </Select>
                            </FormControl>
                        </Box>
                    </div>
                    <div style={{textAlign:'right', paddingBottom:'20px'}}>
                        {id ? <Button variant='contained' onClick={createRoomBtn} >변경</Button> : <Button variant='contained' onClick={createRoomBtn} >방 생성</Button>}
                    </div>
                </div>
            </div>
        </Layout>
    )
}

export default CreateChatRoomForm;