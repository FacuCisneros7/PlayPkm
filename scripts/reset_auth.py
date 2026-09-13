import firebase_admin
from firebase_admin import credentials
from firebase_admin import auth

# 1. Asegúrate de poner el archivo .json descargado en esta misma carpeta
# y que se llame exactamente 'serviceAccountKey.json'
try:
    cred = credentials.Certificate("serviceAccountKey.json")
    firebase_admin.initialize_app(cred)
    print("Conexión con Firebase exitosa.")
except Exception as e:
    print(f"Error al cargar las credenciales: {e}")
    print("Asegúrate de que 'serviceAccountKey.json' esté en la carpeta /scripts/")
    exit()

def delete_all_users():
    confirm = input("¿ESTÁS SEGURO? Esto borrará a TODOS los usuarios de Authentication. (si/no): ")
    if confirm.lower() != 'si':
        print("Operación cancelada.")
        return

    print("Iniciando borrado masivo...")
    count = 0
    # Listar usuarios en lotes de 1000 (límite de Firebase)
    page = auth.list_users()
    while page:
        uids = [user.uid for user in page.users]
        if uids:
            auth.delete_users(uids)
            count += len(uids)
            print(f"Eliminados {len(uids)} usuarios... (Total: {count})")

        page = page.get_next_page()

    print(f"\n¡LISTO! Se han eliminado {count} usuarios.")
    print("Recuerda borrar manualmente las colecciones 'Users', 'attempts' y 'rankings_cache' en Firestore.")

if __name__ == "__main__":
    delete_all_users()
