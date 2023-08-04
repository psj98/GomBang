import React, { useEffect, useRef } from 'react';
import styles from './FilterDetailModal.module.css';

const FilterDetailModal = ({children,closePortal}) => {
    const ref = useRef();
    const [mounted,setMounted] = useState(false);
    useEffect(() => {
        setMounted(true)
        if(document){
            const dom = document.getElementById('modal')
            ref.current = dom
        }
    }, []);

    if(ref.current&&mounted) { // mounted 됬고 dom이 존재하는 경우 모달 랜더링 진행
        return createPortal(
            <div className={styles.modal_container}>
            <div className={styles.modal_background} role="presentation" onClick={closePortal}/>
            {children}
            </div>,
            ref.current
        )
    }
    return null
}

export default FilterDetailModal;