{
  description = "just for lulz";
  inputs.nixpkgs.url = "github:nixos/nixpkgs";
  inputs.flake-utils.url = "github:numtide/flake-utils";

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem
      (system:
        let pkgs = nixpkgs.legacyPackages.${system}; in
        {
          devShells.default = pkgs.mkShell {
            buildInputs = [
              pkgs.sbt
              pkgs.scalafmt
              pkgs.nodejs
            ];
          };
        }
      );
  # see https://serokell.io/blog/practical-nix-flakes
}
