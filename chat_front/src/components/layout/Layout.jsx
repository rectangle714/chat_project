import Header from '@layout/Header';
import Footer from '@layout/Footer';
import Sidebar from '@layout/SideBar';
import styles from '@styles/layout/Layout.module.scss'

const Layout = (props) => {
    return (
        <div className={styles.layout}>
            <Header />
            <div className={styles.body}>
                <div className={styles.sidebar}>
                    <Sidebar />
                </div>
                <main className={styles.main}>
                    {props.children}
                </main>
            </div>
            <Footer />
        </div>
    );
};

export default Layout