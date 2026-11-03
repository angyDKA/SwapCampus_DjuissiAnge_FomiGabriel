import SwiftUI
import ComposeApp

// Bridge: hosts the Compose UI (a UIViewController made in Kotlin) inside SwiftUI.
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea() // Compose handles the safe areas itself (safeDrawingPadding)
    }
}
