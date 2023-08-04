import Header from "../components/Header"
import PopularListContainer from "../components/Main/PopularListContainer"
import RoomFilterForm from "../components/Main/RoomFilterForm"

export default function Main() {
    
    return (
        <div>
            <Header/>
            <div className="main">
                <RoomFilterForm/>
                <PopularListContainer/>
            </div>
        </div>
        
    )
};