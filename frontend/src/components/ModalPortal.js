import React, { useEffect, useRef, useState } from 'react';
import styles from './ModalPortal.module.css'
import {createPortal} from 'react-dom';
const ModalPortal = ({children,closePortal}) => {
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
                <div className={styles.modal_background} role="presentation" onClick={closePortal}>
                    <div className={styles.modal_contents}>{children}</div>
                </div>
            </div>,
            ref.current
        )
    }
    return null
}

export default ModalPortal;